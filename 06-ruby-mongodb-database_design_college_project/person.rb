
class Person
  include Mongoid::Document

  field :first_name, type: String
      validates :first_name,  presence: true, length: { maximum: 50 } ,format: { with: /\A[a-zA-Z]+\z/,
    message: "only allows letters" }

  field :middle_name, type: String

  field	:last_name, type: String

  field	:email, type: String
    validates :email,  presence: true, length: { maximum: 50 } ,format: { with: /\A[\w+\-.]+@[a-z\d\-.]+\.[a-z]+\z/i,
    message: "only allows letters" }

  field :primary_contact, type: String

  field	:secondary_contact, type: String
 

  
  has_and_belongs_to_many :roles

  
  has_and_belongs_to_many :addresses, :dependent => :destroy, autosave: true
  

  has_and_belongs_to_many :expertises, autosave: true

  has_and_belongs_to_many :interests, autosave: true

  has_and_belongs_to_many :educations, autosave: true


  has_and_belongs_to_many :events, autosave: true

  accepts_nested_attributes_for :addresses

  validates_associated :addresses
end
